package fileimport;

//Input class for file I/O
//Child classes will have more methods

public class Input {
	// Instance variable
	private String folderPath;

	// Constructor
	public Input(String folderPath) {
		this.folderPath = folderPath;
	}

	// Copy Constructor
	public Input(Input copy) {
		this.folderPath = copy.folderPath;
	}
	
	// Clone method
	public Input clone() {
		// Override clone method to call copy constructor, passes itself as
		// object & returns duplicate
		return new Input(this);
	}

	// Accessor
	public String getFolderPath() {
		return this.folderPath;
	}

	// Mutator:
	public boolean setFolderPath(String newPath) {
		if ((newPath == null) || (newPath == "")) {

			return false;
		} else {

			this.folderPath = newPath;
			return true;
		}
	}

}