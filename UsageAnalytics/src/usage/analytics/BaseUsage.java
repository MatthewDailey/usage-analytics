package usage.analytics;

import usage.analytics.UsageAnalytics.Usage;

public class BaseUsage implements Usage {

	private final String id;
	private final String installation;
	private final String tag;
	private final String description;
	private final long timestamp;
	
	public BaseUsage(String id, String installation, String tag,
			String description, long timestamp) {
		this.id = id;
		this.installation = installation;
		this.tag = tag;
		this.description = description;
		this.timestamp = timestamp;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String installation() {
		return installation;
	}

	@Override
	public String tag() {
		return tag;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public long timestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "BaseUsage [id=" + id + ", installation=" + installation
				+ ", tag=" + tag + ", description=" + description
				+ ", timestamp=" + timestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((installation == null) ? 0 : installation.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseUsage other = (BaseUsage) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (installation == null) {
			if (other.installation != null)
				return false;
		} else if (!installation.equals(other.installation))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

}
