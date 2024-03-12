import csv
import random

def read_csv(filename):
    with open(filename, mode='r', newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        records = [row for row in reader]
    return records

def write_csv(filename, records, headers):
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=headers)
        writer.writeheader()
        for record in records:
            writer.writerow(record)

# Path to your input CSV file
input_filename = 'OMDb_byIdOrTitle_modified_1000.csv'

# Read the records from the input file
records = read_csv(input_filename)

# Example processing: Shuffle the records
random.shuffle(records)

# Example processing: Extend to 15,000 records, if necessary
# This simplistic approach assumes you want exact replicas to reach 15,000 records
while len(records) < 15000:
    records.extend(records[:15000 - len(records)])

# Headers based on your input file's first row
headers = ['testCaseId', 'queryParameters', 'operationId', 'path', 'httpMethod', 'headerParameters', 'pathParameters', 'formParameters', 'bodyParameter', 'statusCode', 'responseBody']

# Path to your output CSV file
output_filename = 'output.csv'

# Write the processed records to the output file
write_csv(output_filename, records, headers)

print("CSV file has been written with processed records.")
