#!/bin/bash
set -e

echo "=== University ERP build started ==="

# 1. Clean previous build files
echo "Cleaning old build files..."
rm -rf bin
mkdir -p bin
rm -f ERP-System.jar
rm -rf build_temp

# 2. Compile Java files
echo "Compiling Java files..."
find edu -name "*.java" > sources.txt
javac -cp "lib/*" -d bin @sources.txt
rm sources.txt
echo "Compilation successful!"

# 3. Create temp packaging directory
echo "Packaging FAT JAR..."
mkdir -p build_temp
cp -R bin/* build_temp/

# 4. Extract libraries into temp directory
cd build_temp
for jar in ../lib/*.jar; do
    echo "Extracting $jar..."
    jar xf "$jar"
done

# Remove signatures to avoid SecurityException
echo "Removing library signatures..."
rm -rf META-INF/*.SF META-INF/*.DSA META-INF/*.RSA
cd ..

# 5. Create manifest
echo "Creating Manifest..."
echo -e "Manifest-Version: 1.0\nMain-Class: edu.univ.erp.Main\n" > Manifest.txt

# 6. Build the standalone runnable JAR
echo "Building runnable JAR..."
jar cfm ERP-System.jar Manifest.txt -C build_temp .

# 7. Clean up
echo "Cleaning up temp files..."
rm -rf build_temp
rm -f Manifest.txt

echo "=== Build Completed Successfully! ==="
echo "Runnable Fat JAR created at: $(pwd)/ERP-System.jar"
echo "To run the application, make sure the 'resources' folder containing your database configuration is present in the same directory, then run:"
echo "  java -jar ERP-System.jar"
