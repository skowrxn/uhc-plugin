package pl.skowron.uhc.scoreboard;

import java.util.Objects;

public class SidebarEntry {

	public String name;
	public String prefix;
	public String suffix;

	public SidebarEntry(String name) {
		this.name = name;
	}

	public SidebarEntry(Object name) {
		this.name = String.valueOf(name);
	}

	public SidebarEntry(String prefix, String name, String suffix) {
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public SidebarEntry(Object prefix, Object name, Object suffix) {
		this(name);
		this.prefix = String.valueOf(prefix);
		this.suffix = String.valueOf(suffix);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SidebarEntry)) return false;

		SidebarEntry that = (SidebarEntry) o;

		if (!Objects.equals(name, that.name)) return false;
		if (!Objects.equals(prefix, that.prefix)) return false;
		return Objects.equals(suffix, that.suffix);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
		result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
		return result;
	}
}
