def if_rev(dir): 
    os.chdir(dir)
    print(dir)

path = "C:\Users\khanh.phan-minh\Desktop\Learn_Programming"

path_abs = path.replace("\\","\\\\")
is_rev(path_abs)