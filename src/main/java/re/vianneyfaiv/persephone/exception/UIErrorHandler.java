package re.vianneyfaiv.persephone.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Handle Persephone exceptions, like when an application endpoint is not available
 */
public class UIErrorHandler implements ErrorHandler {

	private static final long serialVersionUID = 7441835079387513134L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UIErrorHandler.class);

	@Override
    public void error(com.vaadin.server.ErrorEvent event) {

		// Loop through the exception stack
		for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {

			/*
			 * Technical runtime exceptions
			 */
			if(t instanceof ApplicationRuntimeException) {
				
				LOGGER.error("Error handler: ApplicationRuntimeException", t);

				ApplicationRuntimeException e = (ApplicationRuntimeException) t;

				// Display error notification
				new Notification(
						"Unable to reach " + e.getApplication().getUrl(),
					    e.getMessage(),
					    Notification.Type.ERROR_MESSAGE,
					    true)
					.show(Page.getCurrent());
				return;
			}
			/*
			 * Expected exceptions (not handled)
			 */
			else if(t instanceof ApplicationException) {
				
				LOGGER.error("Error handler: ApplicationException", t);

				ApplicationException e = (ApplicationException) t;

				// Display error notification
				new Notification(
						"Unhandled error. Application " + e.getApplication().getName(),
					    e.getMessage(),
					    Notification.Type.ERROR_MESSAGE,
					    true)
					.show(Page.getCurrent());

				return;
			}
			else if(t instanceof RuntimeException) {
				
				LOGGER.error("Error handler: RuntimeException", t);
				
				RuntimeException e = (RuntimeException) t;

				// Display error notification
				new Notification(
						"Unhandled runtime exception.",
					    e.getMessage(),
					    Notification.Type.ERROR_MESSAGE,
					    true)
					.show(Page.getCurrent());
				
				return;
			
			} else {
				LOGGER.error("Persephone Error Handler: {} ; Message: {}", t.getClass(), t.getMessage());
			}
		}
	}
}
