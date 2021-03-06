



import groovy.json.JsonBuilder
import groovy.json.JsonOutput


def generateDataBagData(String idValue, String verValue, String outputFile ){

	
	def json = new JsonBuilder([id: idValue, version: verValue])
	println json
	
	def fileWriter = new FileWriter(outputFile)
	json.writeTo(fileWriter)
	fileWriter.flush() 
	println "data bag item written to '${outputFile}'"

	
}


/**
Update an existing databag item
**/
def updateDataBag(String dataBagName, String dataBagItem) {

	Process p2 = ["cmd", "/c", "knife", "data", "bag", "from", "file", dataBagName, dataBagItem].execute()
	p2.waitForProcessOutput(System.out, System.err)
	println "Data Bag " + dataBagName +" updated "
}


/**
Update an existing databag item in GIT Repo
**/
def updateLocalDataBag(String dataBagName, String dataBagItem) {

	Process p2 = ["cmd", "/c", "knife", "data", "bag", "from", "file", dataBagName, dataBagItem].execute()
	p2.waitForProcessOutput(System.out, System.err)
	println "Data Bag " + dataBagName +" updated"
}

/** Within the chef GIT repository a data bag item resides under data_bags/Item name
**/
def doFolderCheck(String artefactName){

	//AMD currentDir will be ${WORKSPACE} for now its C:\WorkspaceMS\aibprodgit\ci-chef-repo
	 String currentDir = "C:/WorkspaceMS/aibprodgit/ci-chef-repo"
	 println "current working directory is ${currentDir}"
	 String targetFolder = currentDir + "/data_bags/"+ artefactName
	 def mainDir = new File(targetFolder)
	 if (mainDir.exists() ==false) {
	  println targetFolder + " will be created"
	  mainDir.mkdirs()
	}
	return targetFolder
}

/**
Nothing will have been added to the workspace except for the data bag item 
and possibly the data bag folder under data_bags/
**/
def doGitUpdate(targetFolder, artefactName) {


	//what is my local branch
	//git rev-parse --abbrev-ref HEAD
		Process p= ["cmd", "/c", "git", "rev-parse", "--abbrev-ref", "HEAD"].execute()
	def branch= p.in.text
	println("Branch : " + branch) 

    		
		
		Process pAdd = ["cmd", "/c", "git", "add", "--all"].execute()
		pAdd.waitForProcessOutput(System.out, System.err)
		
		Process pCommit = ["cmd", "/c", "git", "commit", "-m", "data bag update " + artefactName].execute()
		pCommit.waitForProcessOutput(System.out, System.err)
		
		Process pPush = ["cmd", "/c", "git", "push", "origin", branch ].execute()
		pPush.waitForProcessOutput(System.out, System.err)

	
	
}


def myArtefact = "my-artefact-6"
def version = "1.0"

def targetFolder = doFolderCheck(myArtefact)
def targetFile = targetFolder +"/"+ myArtefact +"-" + version+".json"
println "New data bag item will be added to repo " + targetFile

//generateDataBagData(myArtefact, version, targetFile)

//updateDataBag("amd_poc_db", targetFile)

doGitUpdate("C:/WorkspaceMS/github/finance-chef-repo", myArtefact)






