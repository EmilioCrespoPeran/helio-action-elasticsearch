package helio.exceptions;

import helio.blueprints.exceptions.ActionException;

/**
 * Exception defined when there is an error on performing an Elasticsearch operation.
 *
 * @author Emilio Crespo Perán
 */
public class ElasticsearchException extends ActionException {

	public ElasticsearchException(String msg) {
		super(msg);
	}

}
