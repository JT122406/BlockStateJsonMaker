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
    val itemjsonFolder = File(folder, "Item_model_jsons")
    val blockStateFolder = File(folder, "Blockstate_jsons")
    if (!itemjsonFolder.exists()) itemjsonFolder.mkdir()
    if (!blockStateFolder.exists()) blockStateFolder.mkdir()
    

    // Get the list of PNG files in the folder
    val pngFiles = folder.listFiles { _, name -> name.endsWith(".png") }

    if (pngFiles != null)
        for (pngFile in pngFiles) {
            val fileName = pngFile.nameWithoutExtension

            // Create the JSON content
            var jsonContent = """
                {
                  "parent": "${modId}:block/${fileName}"
                }
            """.trimIndent()

            // Write the JSON content to the file
            File(itemjsonFolder, "$fileName.json").writeText(jsonContent)

            jsonContent = """
            {
                "variants": {
                    "facing=north": {
                      "model": "${modId}:block/${fileName}"
                    },
                    "facing=east": {
                      "model": "${modId}:block/${fileName}",
                      "y": 90
                    },
                    "facing=south": {
                      "model": "${modId}:block/${fileName}",
                      "y": 180
                    },
                    "facing=west": {
                      "model": "${modId}:block/${fileName}",
                      "y": 270
                    }
                }
            }
            """.trimIndent()

            File(blockStateFolder, "$fileName.json").writeText(jsonContent)



            println("Created JSON files for: ${fileName}")
        }
    else println("No PNG files found in the folder.")
}
