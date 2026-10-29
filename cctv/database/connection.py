import os
import dotenv
import psycopg2

def connect():
    try:
        dotenv.load_dotenv()

        conn = psycopg2.connect(
            dbname="ironwatchsystem",
            user="postgres",
            password=os.getenv("DB_PASSWORD"),
            host="localhost",
            port=3001
        )

        if not conn.closed:
            print("Connected to the database.")

        else:
            print("Failed to connect to the database.")
            return None

        return conn

    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)
        return None