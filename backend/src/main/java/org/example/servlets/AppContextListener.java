package org.example.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.dao.currency.CurrencyDao;
import org.example.dao.currency.JdbcCurrencyDao;
import org.example.dao.exchange.ExchangeRateDao;
import org.example.dao.exchange.JdbcExchangeRateDao;
import org.example.service.*;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();

        CurrencyDao currencyDao = new JdbcCurrencyDao();
        ExchangeRateDao rateDao = new JdbcExchangeRateDao();
        CurrencyService currencyService = new CurrencyServiceImpl(currencyDao);
        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl(rateDao, currencyDao);
        ExchangeService exchangeService = new ExchangeServiceImpl(rateDao, currencyDao);

        servletContext.setAttribute("currencyService", currencyService);
        servletContext.setAttribute("exchangeRateService", exchangeRateService);
        servletContext.setAttribute("exchangeService", exchangeService);
    }
}