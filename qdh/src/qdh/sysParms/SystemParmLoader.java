package qdh.sysParms;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import qdh.utility.loggerLocal;


/**
 * Application Lifecycle Listener implementation class SystemParmLoader
 *
 */
public class SystemParmLoader implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public SystemParmLoader() {
    	loggerLocal.info("Starting the SystemParmLoader ...");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
      
        loggerLocal.info("开始加载消息管理器 ...");
        QXMsgManager.load();
        
        loggerLocal.info("开始加载免拦截url ...");
        DefaultFunction.load();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
