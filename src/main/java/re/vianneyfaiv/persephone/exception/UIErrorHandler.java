package re.vianneyfaiv.persephone.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Handle Persephone exceptions and display them in error popups
 */
public class UIErrorHandler implements ErrorHandler {

	private static final long serialVersionUID = 7441835079387513134L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UIErrorHandler.class);

	@Override
    public void error(com.vaadin.server.ErrorEvent event) {

		// Loop through the exception stack
		for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {

			// Try to get a persephone exception
			boolean exceptionHandled = handlePersephoneExceptions(t);

			// If no persephone exception has been found => try from Exception#getCause
			if(!exceptionHandled) {
				exceptionHandled = handlePersephoneExceptions(t.getCause());
			}

			if(exceptionHandled) {
				return;
			} else if(t instanceof RuntimeException) {
				displayErrorNotif("Unhandled runtime exception.", t);
				return;
			} else {
				LOGGER.error("Persephone Error Handler: {} ; Message: {}", t.getClass(), t.getMessage());
			}
		}
	}

	private boolean handlePersephoneExceptions(Throwable t) {
		boolean handled = false;

		// Technical runtime exceptions
		if(t instanceof ApplicationRuntimeException) {
			ApplicationRuntimeException e = (ApplicationRuntimeException) t;
			displayErrorNotif("Unable to reach " + e.getApplication().getUrl(), e);
			handled = true;
		}
		else if(t instanceof ApplicationNotFoundException) {
			ApplicationNotFoundException e = (ApplicationNotFoundException) t;
			displayErrorNotif(e.getMessage(), e);
			handled = true;
		}
		// Expected exceptions (not handled)
		else if(t instanceof ApplicationException) {
			ApplicationException e = (ApplicationException) t;
			displayErrorNotif("Unhandled error. Application " + e.getApplication().getName(), e);
			handled = true;
		}

		return handled;
	}

	private void displayErrorNotif(String msg, Throwable t) {
		   LOGGER.error(String.format("Error handler: %s", msg), t);

		   if(!StringUtils.isEmpty(t.getMessage())) {
		           new Notification(
		                   msg,
		                   t.getMessage(),
		                   Notification.Type.ERROR_MESSAGE,
		                   false)
		                   .show(Page.getCurrent());
		       }
		}
}
