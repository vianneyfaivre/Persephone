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

			if(t instanceof PersephoneServiceException) {

				PersephoneServiceException e = (PersephoneServiceException) t;

				// Display error notification
				new Notification(
						"Unable to reach " + e.getApplication().getUrl(),
					    e.getMessage(),
					    Notification.Type.ERROR_MESSAGE,
					    true)
					.show(Page.getCurrent());
			}
			else {
				LOGGER.error("Exception class: {} ; Message: {}", t.getClass(), t.getMessage());
			}
		}
	}
}
