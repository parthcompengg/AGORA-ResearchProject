import pandas as pd
from google.colab import files

# Load the dataset
df = pd.read_csv("Spotify_GetArtistAlbums_10K.csv")

def limit_duplicates(df, subset, threshold=5):
    return df.groupby(subset, as_index=False).head(threshold)

limited_df = limit_duplicates(df, ['operationId', 'path', 'httpMethod'])

limited_df.to_csv('Spotify_GetArtistAlbums_10K.csv', index=False)

# Print the grouped DataFrame

# Save the grouped DataFrame to a CSV file
#grouped_df.to_csv('ddYelp10k.csv', index=False)

# Download the CSV file
files.download('Spotify_GetArtistAlbums_10K.csv')
