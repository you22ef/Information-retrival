# Inverted Index Project

## Overview
This project implements an **Inverted Index** in Java, a fundamental data structure used in information retrieval systems. The inverted index allows for efficient text search by mapping terms to their occurrences in a collection of documents.

## Features
- Builds an inverted index from a collection of text files.
- Stores and retrieves index data from a file.
- Supports basic boolean and phrase search queries.
- Implements text normalization techniques such as stemming and stop-word removal.

## File Structure
- `Test.java`: Main class to test the indexing and search functionality.
- `Index5.java`: Core class that builds, stores, and retrieves the inverted index.
- `DictEntry.java`: Represents dictionary entries, storing term frequency and posting lists.
- `Posting.java`: Represents linked list nodes for the posting lists.
- `SourceRecord.java`: Stores metadata about documents in the collection.
- `Stemmer.java`: Implements the Porter Stemming algorithm for word normalization.

## How It Works
1. **Build the Index**
   - Reads text files from a specified directory.
   - Tokenizes words, removes stop words, and applies stemming.
   - Creates a dictionary of terms with their respective posting lists.

2. **Search**
   - Implements Boolean search model to find documents containing query terms.
   - Supports phrase search to find exact word sequences.

3. **Store & Load Index**
   - Stores the inverted index in a file (`data.txt`) for persistence.
   - Allows reloading of the index without rebuilding.

## Prerequisites
- Java Development Kit (JDK 8+)

## Compilation & Execution
1. **Compile the Java files**
   - javac invertedIndex/*.java

2. **Run the test program**
   - java invertedIndex.Test

## Example Usage
=============EBD STORE=============
** [youssef,1]       =--> [1]
** [ahmed,1]       =--> [2]
** [hassan,1]       =--> [2]
** [atya,1]       =--> [1]
** [mohamed,2]       =--> [1,2]
** [ali,1]       =--> [1]
------------------------------------------------------
*** Number of terms = 6
Boo0lean Model result =
        1 - D:\Information retrival\is322_HW_1\is322_HW_1\src\invertedIndex\Path\index.txt - 4
