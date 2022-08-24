package helio.actions.database.exceptions;

import helio.blueprints.exceptions.ActionException;

/**
 * Exception defined when there is an error in configuration.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchConfigurationException extends ActionException {

	public ElasticsearchConfigurationException(String msg) {
		super(msg);
	}

}
