require 'aws-sdk-s3'
require 'pry'
require 'securerandom'

bucket_name = ENV['BUCKET_NAME']
region = 'us-east-1'
puts "Bucket Name: #{bucket_name}"

client = Aws::S3::Client.new(region: region)

begin
  # Create the S3 bucket
  client.create_bucket({
    bucket: bucket_name
  })
  puts "Bucket '#{bucket_name}' created successfully."
rescue Aws::S3::Errors::BucketAlreadyExists
  puts "Bucket '#{bucket_name}' already exists."
rescue Aws::S3::Errors::BucketAlreadyOwnedByYou
  puts "Bucket '#{bucket_name}' is already owned by you."
end

# Wait for the bucket to be fully initialized
sleep(5)

number_of_files = 1 + rand(6)
puts "Creating #{number_of_files} files"

number_of_files.times do |i|
  puts "Creating file #{i}"
  file_name = "file_#{i}.txt"
  output_path = "/tmp/#{file_name}"

  # Write random content to the file
  File.open(output_path, 'w') do |file|
    file.write(SecureRandom.uuid)
  end

  # Upload the file to S3
  File.open(output_path, 'r') do |file|
    client.put_object(
      bucket: bucket_name,
      key: file_name,
      body: file
    )
  end
end

puts "#{number_of_files} files successfully uploaded to bucket '#{bucket_name}'."
