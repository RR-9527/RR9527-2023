function fail() {
    echo -e "\nFailed; exiting...\n"
    exit 1
}

# Gets the path of the file the script is currently in
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" || fail ; pwd -P )

# Changes directory to the TeamCode source files
# This is agnostic to the location of where the script was called;
# it will always run relative to the script's location
cd "$parent_path" || fail

./gradlew clean
./gradlew build