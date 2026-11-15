from pathlib import Path as path
import os
from dotenv import load_dotenv

load_dotenv()
os.chdir(os.getenv("WORKING_DIR"))

images_dir_path = path(path.cwd().joinpath('resources/assets/images').resolve())
images = images_dir_path.iterdir()

ext = "jpg"

renames = {
	"thembinomsamnisi": "thembijosephinemnisi",
	"gugunomsashongwe": "gugumtshalishongwe",
}

for image in images:
	stem = image.stem
	if stem in renames:
		new_image_path = image.with_name(renames[stem] + "." + ext)
		data = image.read_bytes()
		image.unlink()
		new_image_path.write_bytes(data)
		print(f"{image.name} -> {new_image_path.name}")