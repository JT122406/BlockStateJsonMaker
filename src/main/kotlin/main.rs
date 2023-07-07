use std::env;
use std::fs::{self, File};
use std::io::Write;
use std::path::Path;

fn main() {
    // Prompt for the mod ID
    println!("Enter the mod ID:");
    let mut mod_id = String::new();
    std::io::stdin().read_line(&mut mod_id).expect("Failed to read input");
    let mod_id = mod_id.trim();

    // Prompt for the folder path
    println!("Enter the folder path:");
    let mut folder_path = String::new();
    std::io::stdin().read_line(&mut folder_path).expect("Failed to read input");
    let folder_path = folder_path.trim();

    // Create a new folder for the JSON files
    let json_folder = Path::new(folder_path).join("json");
    if !json_folder.exists() {
        fs::create_dir(&json_folder).expect("Failed to create json folder");
    }

    // Get the list of PNG files in the folder
    let png_files: Vec<_> = fs::read_dir(folder_path)
        .expect("Failed to read folder")
        .filter_map(|entry| {
            let entry = entry.expect("Failed to read entry");
            let path = entry.path();
            if path.is_file() && path.extension().map(|ext| ext == "png").unwrap_or(false) {
                Some(path)
            } else {
                None
            }
        })
        .collect();

    if !png_files.is_empty() {
        for png_file in png_files {
            let file_name = png_file.file_stem().expect("Failed to get file name");
            let json_file_name = format!("{}.json", file_name.to_string_lossy());
            let json_file_path = json_folder.join(&json_file_name);

            // Create the JSON content
            let json_content = format!(
                r#"{{
  "variants": {{
    "": {{
      "model": "{}:block/{}"
    }}
  }}
}}"#,
                mod_id,
                file_name.to_string_lossy()
            );

            // Write the JSON content to the file
            let mut json_file =
                File::create(&json_file_path).expect("Failed to create JSON file");
            json_file
                .write_all(json_content.as_bytes())
                .expect("Failed to write JSON content");

            println!("Created JSON file: {}", json_file_path.display());
        }
    } else {
        println!("No PNG files found in the folder.");
    }
}
