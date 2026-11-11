from pathlib import Path as path
import os
from dotenv import load_dotenv

load_dotenv()

def get_url(name):
	os.chdir(os.getenv("WORKING_DIR"))
	
	images_dir_path = path(path.cwd().joinpath('resources/assets/images').resolve())
	images = os.listdir(images_dir_path)
	
	name = name.lower()
	
	for image in images:
		nm = image[0:image.find(".")]
		
		if "".join(sorted(nm)).lower() == "".join(sorted(name)).lower():
			image_path = images_dir_path.joinpath(image)
			return str(image_path)