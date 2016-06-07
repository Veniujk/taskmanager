package pl.kielce.tu.kronos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Bartosz Piskiewicz
 * Class carrying response data from server application
 */
public class Response implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private short code;
	private Date responseTime;
	private String JSONdata;
	
	/**
	 * Creates new response.
	 * @param code Http code informing about request status.
	 * @param data Data send as a response.
	 */
	public Response(short code, String JSONdata) {
		this.code = code;
		this.JSONdata = JSONdata;
		this.responseTime = new Date();
	}
	
	public Response(short code){
		this.code = code;
		this.JSONdata = null;
		this.responseTime = new Date();
	}
	/**
	 * 
	 * @return Gets http response code.
	 */
	public short getCode() {
		return code;
	}

	/**
	 * 
	 * @return Time, when the data were sent.
	 */
	public Date getResponseTime() {
		return responseTime;
	}

	/**
	 * 
	 * @return requested data.
	 */
	public String getJSONdata() {
		return JSONdata;
	}
}
