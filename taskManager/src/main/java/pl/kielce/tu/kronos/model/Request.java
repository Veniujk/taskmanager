package pl.kielce.tu.kronos.model;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {

	private static final long serialVersionUID = 1L;
	private String resource;
	private String method;
	private String JSONdata;		
	private Date dataGetTime;
	
	public Request(String resource, String method, String JSONdata) {
		this.resource = resource;
		this.method = method;
		this.JSONdata = JSONdata;
	}
	
	public Request(String resource, String method) {
		this.resource = resource;
		this.method = method;
		this.JSONdata = "{}";
	}
	
	public Request(String resource, String method, String JSONdata, Date dataGetTime){
		this.resource = resource;
		this.method = method;
		this.JSONdata = JSONdata;
		this.dataGetTime = dataGetTime;
	}

	public String getResource() {
		return resource;
	}

	public String getMethod() {
		return method;
	}

	public String getJSONdata() {
		return JSONdata;
	}
	
	
	
}
