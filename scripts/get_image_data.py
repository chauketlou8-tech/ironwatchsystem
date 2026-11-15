import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

from cctv.database.connection import connect
from PIL import Image

def get_data():
	connection = connect()
	cursor = connection.cursor()
	
	try:
		cursor.execute("select profile_picture_path from employees")
		
		urls = [url[0] for url in cursor.fetchall()]
		
		for url in urls:
			with Image.open(url) as img:
				print(url, img.size)
		
	except Exception:
		connection.rollback()
		throw
		
	finally:
		connection.close()
		
get_data()