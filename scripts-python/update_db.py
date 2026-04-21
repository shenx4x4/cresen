import psycopg2
import getpass

def run_update():
    try:
        conn = psycopg2.connect(
            host="localhost", 
            database="cresen_db", 
            user=getpass.getuser()
        )
        cursor = conn.cursor()

        # Update Tabel Users
        cursor.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS balance DOUBLE PRECISION DEFAULT 0.0;")
        
        # Buat Tabel Products
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS products (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100),
                price DOUBLE PRECISION,
                stock INTEGER
            );
        """)

        # Isi Produk Contoh (Opsional)
        cursor.execute("SELECT COUNT(*) FROM products")
        if cursor.fetchone()[0] == 0:
            cursor.execute("INSERT INTO products (name, price, stock) VALUES (%s, %s, %s)", ('Knalpot Tyga NSR', 25000, 5))

        conn.commit()
        print("[✓] Database cresen_db Berhasil Diupdate!")
        cursor.close()
        conn.close()
    except Exception as e:
        print(f"[X] Gagal Update: {e}")

if __name__ == "__main__":
    run_update()
  
