import java.io.File

fun main() {
    // Prompt for the mod ID
    println("Enter the mod ID:")
    val modId = readLine() ?: return

    // Prompt for the folder path
    println("Enter the folder path:")
    val folderPath = readLine() ?: return

    // Create a File object for the folder
    val folder = File(folderPath)

    // Create a new folder for the JSON files
    val jsonFolder = File(folder, "json")
    if (!jsonFolder.exists()) jsonFolder.mkdir()
    

    // Get the list of PNG files in the folder
    val pngFiles = folder.listFiles { _, name -> name.endsWith(".png") }

    if (pngFiles != null)
        for (pngFile in pngFiles) {
            val fileName = pngFile.nameWithoutExtension
            val jsonFile = File(jsonFolder, "$fileName.json")

            // Create the JSON content
            val jsonContent = """
                {
                  "variants": {
                    "": {
                      "model": "$modId:block/$fileName"
                    }
                  }
                }
            """.trimIndent()

            // Write the JSON content to the file
            jsonFile.writeText(jsonContent)

            println("Created JSON file: ${jsonFile.absolutePath}")
        }
    else println("No PNG files found in the folder.")
}
