package org.powertac.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.powertac.common.Broker;
import org.powertac.common.IdGenerator;
import org.powertac.common.XMLMessageConverter;
import org.powertac.common.interfaces.BrokerProxy;
import org.powertac.common.repo.BrokerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerMessageReceiver implements MessageListener
{
  static private Logger log = Logger.getLogger(ServerMessageReceiver.class);

  @Autowired
  private XMLMessageConverter converter;

  @Autowired 
  private BrokerProxy brokerProxy;

  @Autowired
  private VisualizerProxyService visualizerProxy;

  @Autowired
  private BrokerRepo brokerRepo;

  private Pattern brokerRegex = Pattern.compile("<broker>([A-Za-z0-9_ ]+)</broker>");
  private Pattern idRegex = Pattern.compile(" id=\"([0-9]+)\"");

  @Override
  public void onMessage (Message message)
  {
    if (message instanceof TextMessage)
      onMessage((TextMessage)message);
    else
      log.warn("Unable to process incoming message of type " + 
               message.getClass().getName());
  }

  public void onMessage (TextMessage message)
  {
    try {
      log.debug("onMessage(Message) - receiving a message");
      onMessage(message.getText());
    } catch (JMSException e) {
      log.error("failed to extract text from TextMessage", e);
    }
  }

  void onMessage (String xml) {
    // validate broker's key, then strip it off
    String validXml = xml;
    if (xml.startsWith("<broker-authentication")) {
      // don't validate the broker-authentication messages
      validXml = xml;
    }
    else if (xml.startsWith("<visualizer-status")) {
      // visualizer ping request
      log.info("received visualizer ping request");
      visualizerProxy.respondToPing();
      return;
    }
    else {
      // complain if message spoofed or missing validation prefix
      validXml = validateBrokerPrefix(xml);
      if (null == validXml) {
        log.warn("Invalid message: ignoring " + xml);
        return;
      }
    }
    log.debug("onMessage(String) - received message:\n" + validXml);
    Object message = converter.fromXML(validXml);
    log.debug("onMessage(String) - received message of type " + message.getClass().getSimpleName());
    brokerProxy.routeMessage(message);
  }
  
  // check the message prefix against the broker. If it matches, then return
  // the message with the prefix stripped off.
  private String validateBrokerPrefix (String message)
  {
    int realMsg = message.indexOf('<');
    if (0 == realMsg)
      return null;
    String prefix = message.substring(0, realMsg);
    log.debug("prefix=" + prefix);
    Matcher m = brokerRegex.matcher(message);
    if (m.find(realMsg)) {
      String username = m.group(1);
      log.debug("broker username=" + username);
      Broker broker = brokerRepo.findByUsername(username);
      if (broker.getKey().equals(prefix)) {
        // prefix match - check id prefix
        m = idRegex.matcher(message);
        if (m.find(realMsg)) {
          long idValue = Long.parseLong(m.group(1));
          log.debug("message id: " + idValue);
          int idPrefix = IdGenerator.extractPrefix(idValue);
          if (broker.getIdPrefix() == idPrefix) {
            return message.substring(realMsg);
          }
        }
        else {
          // message with no id?
          log.warn("Incoming message with no object id: " + message);
          return message.substring(realMsg);
        }
      }
    }
    return null;
  }
}
