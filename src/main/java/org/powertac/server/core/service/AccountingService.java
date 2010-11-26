package org.powertac.server.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.powertac.server.core.command.PublishTariffCommand;
import org.powertac.server.core.command.RevokeTariffCommand;
import org.powertac.server.core.command.UpdateTariffCommand;

public class AccountingService {

    private final Log log = LogFactory.getLog(AccountingService.class);

    public Boolean publishTariff(PublishTariffCommand ptc) {
        //Todo database create tariff entry
        log.error("Publish tariff " + ptc.getTariffId() + " of broker " + ptc.getAuthToken());
        return true;
    }

    public Boolean revokeTariff(RevokeTariffCommand rtc) {
        //Todo database delete tariff entry
        log.error("Revoke tariff " + rtc.getTariffId() + " of broker " + rtc.getAuthToken());
        return true;
    }

    public Boolean updateTariff(UpdateTariffCommand utc) {
        //Todo database update tariff entry
        log.error("Update tariff " + utc.getTariffId() + " of broker " + utc.getAuthToken());
        return true;
    }

}
