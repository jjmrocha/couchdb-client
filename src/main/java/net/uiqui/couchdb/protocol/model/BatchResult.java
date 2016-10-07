package net.uiqui.couchdb.protocol.model;

import com.google.gson.annotations.SerializedName;

public class BatchResult {
	@SerializedName("ok") private Boolean success = null;
	private String id = null;
	private String rev = null;
	private String error = null;
	private String reason = null;
	
	public boolean isSuccess() {
		return success != null && success;
	}

	public String id() {
		return id;
	}

	public void id(final String id) {
		this.id = id;
	}

	public String rev() {
		return rev;
	}

	public void rev(final String rev) {
		this.rev = rev;
	}
	
	public String error() {
		return error;
	}

	public void error(String error) {
		this.error = error;
	}

	public String reason() {
		return reason;
	}

	public void reason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BatchResult(success=");
		builder.append(isSuccess());
		builder.append(", id=");
		builder.append(id);
		builder.append(", rev=");
		builder.append(rev);
		builder.append(", error=");
		builder.append(error);
		builder.append(", reason=");
		builder.append(reason);
		builder.append(")");
		return builder.toString();
	}
}
