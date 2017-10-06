package re.vianneyfaiv.persephone.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Overrides default Vaadin error handler.
 */
public class UIErrorHandler extends DefaultErrorHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UIErrorHandler.class);

	@Override
    public void error(com.vaadin.server.ErrorEvent event) {

		// Loop through the exception stack
		for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {

			/*
			 * Technical runtime exceptions
			 */
			if(t instanceof ApplicationRuntimeException) {
				
				LOGGER.error("Error handler: PersephoneTechnicalException", t);

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
				
				LOGGER.error("Error handler: PersephoneException", t);

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
