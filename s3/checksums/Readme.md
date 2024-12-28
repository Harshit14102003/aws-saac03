## create a new s3 bucket
```md
aws s3 mb s3://checksums-example-2021a7ps2073g
```

## create a file that we will do checksum on
```md
echo "Hello Mars" > myfile.txt
```

## Get a checksum of the file
```
md5sum myfile.txt
```

## upload our file to s3
```
aws s3 cp myfile.txt s3://checksums-example-2021a7ps2073g
aws s3api get-object --bucket checksums-example-2021a7ps2073g --key myfile.txt outfile && cat outfile && rm outfile
``` 

## Lets upload a file with a different kind of checksum
```sh
    echo -n "$(sha1sum myfile.txt | awk '{print $1}')" | xxd -r -p | base64
```

```sh
  aws s3api put-object \
  --bucket checksums-example-2021a7ps2073g \
  --key myfilesha1.txt \
  --body myfile.txt \
  --checksum-algorithm="SHA1" \
  --checksum-sha1="wozMLF4hQDaAYBTfn7Q2NPPncLI="
```