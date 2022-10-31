if cd ../TeamCode/src/main ; then
  while IFS= read -r -d '' dir; do
     while IFS= read -r -d '' file; do
        mv "$file" "$file.bak"
     done < <( find "$dir" \( -name '*.java' -or -name '*.kt' \) -print0 )
  done < <( find . -type d -name "deprecated" -print0 )
fi
