parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" || fail ; pwd -P )
cd "$parent_path"/../../.. || fail

cp "TunableAutoGen/src/main/java/org/tunableautogen/builder/ActualTuningAutoBuilder.java" "TunableAutoGen/src/main/resources/ActualTuningAutoBuilder.java"

ts-node "TunableAutoGen/scripts/ts/generate_auto_builder_stubs.ts"

ts-node "TunableAutoGen/scripts/ts/generate_actual_tuning_auto_builder.ts"
