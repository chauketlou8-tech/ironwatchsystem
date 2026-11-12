import sys
from pathlib import Path
import os

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

from seed_employees import seed_employees
import bcrypt
from cctv.database.connection import connect

def seed_users():
	employees = seed_employees()
	
	non_users = ["Security Officer", "Armed Response Officer", "Patrol Officer", "Access Control Officer", "Incident Response Officer", "Security Risk Assessment Officer"]
	connection = connect()
	cursor = connection.cursor()
	
	try:
		#for now there are 97/148 users
		for employee in employees:
			if employee["position"] in non_users:
				continue
			
			firstname = employee["firstname"]
			lastname = employee["lastname"]
			email = employee["email"]
			position = employee["position"]
			password = bcrypt.hashpw((email[0:email.find("@") + 1] + str(employee["address_id"])).encode(), bcrypt.gensalt())
			image_url = employee["image_url"]
			
			cursor.execute("select 1 from users where email = %s;", (email,))
			
			exist_user = cursor.fetchone()
			
			if exist_user:
				print("user already exists skipping...")
				continue
				
			cursor.execute('insert into users(firstname, lastname, email, password, position, profile_picture_path)'
			               'values(%s, %s, %s, %s, %s, %s) returning 1',
			               (firstname, lastname, email, password, position, image_url))
			
			added = cursor.fetchone() is not None
			
			if added:
				print("user added!")
				
			else:
				print("problem adding user")
			
	except Exception:
		connection.rollback()
		raise
	
	else:
		connection.commit()
	
	finally:
		connection.close()
		cursor.close()
		
seed_users()