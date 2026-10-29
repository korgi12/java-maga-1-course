package ru.maga.musicweb;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public final class HibernateShutdown implements ServletContextListener {
    @Override public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.factory().close();
    }
}
