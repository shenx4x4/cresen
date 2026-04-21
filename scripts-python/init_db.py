import psycopg2
import bcrypt
import getpass

def setup_cresen_db():
    try:
        # getpass.getuser() akan otomatis mengambil username Termux (misal: u0_a123)
        current_user = getpass.getuser()
        conn = psycopg2.connect(host="localhost", database="cresen_db", user=current_user)
        cursor = conn.cursor()

        cursor.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL
            );
        """)

        cursor.execute("SELECT id FROM users WHERE username = 'cresen_admin'")
        if not cursor.fetchone():
            pwd = "CresenPassword123".encode('utf-8')
            hashed_pwd = bcrypt.hashpw(pwd, bcrypt.gensalt()).decode('utf-8')

            cursor.execute("""
                INSERT INTO users (username, email, password, role)
                VALUES (%s, %s, %s, %s)
            """, ("cresen_admin", "admin@cresen.local", hashed_pwd, "ROLE_ADMIN"))
            print("[✓] Cresen Admin created! (User: cresen_admin | Pass: CresenPassword123)")

        conn.commit()
        cursor.close()
        conn.close()
        print("[✓] Database cresen_db is ready.")
    except Exception as e:
        print(f"[X] Database Setup Error: {e}")

if __name__ == "__main__":
    setup_cresen_db()
  
