import pandas as pd

# Full path to your CSV file
file_path = r'E:\Ajay Courses\OSU Courses\OSU 5th Term Courses\CS 569 Software Evaluaton And Analysis\AGORA Group Project\OMDB API DATASETS AND PYTHON FILES\data.csv'

# Read the first 20,000 records
df = pd.read_csv(file_path, usecols=['original_title'], nrows=20000)

# Extract the movie names into a list
movie_names = df['original_title'].tolist()

# Convert the list to the desired format (with quotes and separated by commas)
formatted_movie_names = [f'"{name}"' for name in movie_names]

def divide_chunks(l, n):
    # Looping till length l
    for i in range(0, len(l), n):
        yield l[i:i + n]

# Number of movies per line
n = 7

# Dividing the movie names into chunks of 7
movie_chunks = list(divide_chunks(formatted_movie_names, n))

# Joining each chunk of movies with a comma and each group with a newline
formatted_string = ",\n".join([", ".join(chunk) for chunk in movie_chunks])

# Printing the formatted string
print("[" + formatted_string + "]")
