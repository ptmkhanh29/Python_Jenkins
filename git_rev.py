import os
import subprocess

dir = r"C:\Users\khanh.phan-minh\Desktop\Learn_Programming"
module = "docker-books"
# Thay đổi thư mục làm việc hiện tại
path = os.path.join(dir, module)
os.chdir(path)

# Lấy revision bằng lệnh git rev-parse HEAD
result = subprocess.run(['git', 'rev-parse', 'HEAD'], capture_output=True, text=True)

# Kiểm tra kết quả và ghi vào file Revision.txt
if result.returncode == 0:
    revision = result.stdout.strip()
    with open(r"C:\Users\khanh.phan-minh\Desktop\Learn_Programming\Revision.txt", "w") as file:
        file.write(revision)
    print("Lấy revision thành công và đã ghi vào file Revision.txt.")
else:
    print("Không thể lấy revision.")
