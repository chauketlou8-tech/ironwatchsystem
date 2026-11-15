import os
from PIL import Image
from pathlib import Path as path
from dotenv import load_dotenv

load_dotenv()

os.chdir(os.getenv("WORKING_DIR"))

images_dir_path = path(path.cwd().joinpath('resources/assets/images').resolve())
images = os.listdir(images_dir_path)

for file in images:
	file_path = os.path.join(images_dir_path, file)
	
	if not os.path.exists(file_path):
		print(f"Missing: {file}")
		continue
	
	try:
		with Image.open(file_path) as img:
			rgb_img = img.convert("RGB")
			temp_path = file_path + ".tmp.jpg"
			rgb_img.save(temp_path, "JPEG", quality=95)
			os.remove(file_path)
			os.rename(temp_path, file_path)
			print(f"Converted: {file}")
	
	except Exception as e:
		print(f"Failed {file}: {e}")

print("Done.")