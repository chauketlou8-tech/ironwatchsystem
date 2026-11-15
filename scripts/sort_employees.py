from manage_image_url import get_url
from datetime import datetime

def sort(employees):
	output = []
	address_id = 1
	
	for employee in employees:
		given_name = employee["firstname"]
	
		given_parts = given_name.split(" ")
		
		first_name = given_parts[0]
		first_middle = given_parts[1] if len(given_parts) > 1 else None
		second_middle = given_parts[2] if len(given_parts) > 2 else None
		
		lastname = employee["lastname"]
		email = "".join(lastname.lower().split(" ") + first_name.lower().split(" ")) + "@ironwatch.co.za"
		
		gender = employee["gender"].upper()
		phone = employee["phone"]
		dob = str(datetime.strptime(employee["dob"], "%Y-%m-%d").date())
		department = employee["department"]
		position = employee["position"]
		position = position[0:position.find("(")].strip() if "(" in position else position
		
		if len(given_parts) == 1:
			full_name = "".join(first_name.lower().split(" ") + lastname.lower().split(" "))
			image_url = get_url(full_name)
		
		elif len(given_parts) == 2:
			full_name = "".join(first_name.lower().split(" ") + first_middle.lower().split(" ") + lastname.lower().split(" "))
			image_url = get_url(full_name)
		
		elif len(given_parts) == 3:
			full_name = "".join(first_name.lower().split(" ") + second_middle.lower().split(" ") + lastname.lower().split(" "))
			image_url = get_url(full_name)
		
		else:
			image_url = None
		
		salary = employee["salary"]
		hire_date = str(datetime.strptime(str(datetime.now())[0:str(datetime.now()).find(" ")], "%Y-%m-%d").date())
		
		output.append({
			"address_id": address_id,
			"firstname": given_name,
			"lastname": lastname,
			"email": email,
			"gender": gender,
			"phone": phone,
			"dob": dob,
			"department": department,
			"position": position,
			"hire_date": hire_date,
			"salary": salary,
			"image_url": image_url,
		});
		
		address_id += 1
		
	return output