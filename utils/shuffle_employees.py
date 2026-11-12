import random

def shuffle(employees):

	PERSONAL = ['firstname', 'lastname', 'email', 'gender', 'phone', 'dob', 'image_url']
	SLOT = ['department', 'position', 'hire_date', 'salary']
	
	first = employees[0]
	rest = employees[1:]
	
	personal_data = [{k: p[k] for k in PERSONAL} for p in rest]
	
	random.seed(42)
	random.shuffle(personal_data)
	
	shuffled = [first]
	for slot, person in zip(rest, personal_data):
		new_record = {'address_id': slot['address_id']}
		new_record.update(person)
		for k in SLOT:
			new_record[k] = slot[k]
		shuffled.append(new_record)
	
	
	return shuffled