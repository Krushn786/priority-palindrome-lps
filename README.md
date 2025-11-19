# A Novel O(n) Algorithm for the Longest Palindromic Substring

This repository presents a **new O(n) algorithm** for the classic  
**Longest Palindromic Substring (LPS)** problem.

Unlike Manacher’s algorithm, which achieves linear time through symmetry and mirror propagation,  
this approach achieves **O(n)** using three simple ideas:

- **String transformation** with separators
- **Best-case potential** bound for each center
- **Center-outward priority ordering** with **early termination**

It remains intuitive and uses only straightforward expand-around-center logic.

All implementation, explanation, and analysis are provided here.

---

## 📌 Repository Structure

priority-palindrome-lps/
│
├── src/
│ ├── Solution.java # Main O(n) algorithm
│ └── Benchmark.java # Runtime tester
│
├── docs/
│ └── paper.md # Full academic-style explanation
│
├── CITATION.cff # Citation metadata (GitHub auto-detects this)
├── LICENSE # MIT License
└── README.md # Project overview (this file)

---

## 🚀 High-Level Algorithm Overview

1. **Transform the string** by inserting `#` between characters  
   → This unifies even/odd palindromes.

2. **Compute bestCase[i] for each center**  
bestCase[i] = 2 * min(i, n - 1 - i) + 1

This forms a symmetric pyramid:  
`1, 3, 5, …, largest …, 5, 3, 1`

3. **Generate a priority traversal order**  
mid, mid-1, mid+1, mid-2, mid+2, ...

This implicitly visits centers in descending best-case potential.

4. **Expand around each center**  
Update the best palindrome found.

5. **Early terminate** when:
bestCase[idx] <= bestTransLen

At this point, no remaining center can improve the answer.

A full detailed explanation is provided in  
**docs/paper.md**

---

## ⏱️ Complexity (Informal)

|             Step          | Time |
|---------------------------|------|
|      Transform string     | O(n) |
|    bestCase computation   | O(n) |
| Priority order generation | O(n) |
|      Total expansion      | O(n) amortized via early termination |

### **Total Time:** O(n)  
### **Space Complexity:** O(n)

---

## 💻 How to Run

From the project root:

```bash
cd src
javac Solution.java Benchmark.java
java Solution       # Runs sample tests
java Benchmark      # Runs performance tests
📄 Citation
If you refer to or build on this work, please cite it.

GitHub will automatically generate a citation entry using CITATION.cff.

⚖️ License
This project is distributed under the MIT License.
See LICENSE for details.

✍️ Author
Krushna Gor
Creator of this priority-based O(n) algorithm for LPS.