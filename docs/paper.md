# A Novel O(n) Approach to the Longest Palindromic Substring  
**Author:** Krushn Gor  

---

## 1. Introduction

The **Longest Palindromic Substring (LPS)** problem asks:

> Given a string \( s \), find the longest contiguous substring of \( s \) that is a palindrome.

Classical solutions include:
- Expand-around-center (O(n²))
- Dynamic programming (O(n²))
- **Manacher’s algorithm (O(n))**, which uses symmetry-based radius propagation

Manacher’s algorithm is powerful but conceptually complex and unintuitive for many developers.

This paper presents a **different O(n) algorithm** using:
- expand-around-center logic,
- a **best-case potential function**,
- **priority ordering**, and
- **early termination**.

The algorithm is simple, intuitive, and still achieves **linear time**.

---

## 2. String Transformation

To unify even and odd-length palindromes, we transform the string by inserting a separator (`#`):

"abba" → "#a#b#b#a#"
"racecar" → "#r#a#c#e#c#a#r#"

- Every palindrome becomes odd-length.
- The transformed string length becomes:  
  \[
  n = 2n_{orig} + 1
  \]

---

## 3. Best-Case Potential Function

For each index \( i \) in the transformed string:

\[
\text{bestCase}[i] = 2 \cdot \min(i,\; n - 1 - i) + 1
\]

This represents the **maximum possible palindrome** around center \( i \), because expansion cannot pass beyond the string’s boundaries.

Example (n = 15):

Index: 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
bestCase: 1 3 5 7 9 11 13 15 13 11 9 7 5 3 1

This forms a symmetric **pyramid pattern**.  
Centers near the middle can potentially form longer palindromes.

---

## 4. Center-Outward Priority Ordering (O(n), No Sorting)

Since centers with higher bestCase values are more promising, we want to visit them first.

Instead of sorting, we generate the correct priority order **in O(n)**:

mid, mid-1, mid+1, mid-2, mid+2, ...

This order **exactly matches** sorting by bestCase in descending order.

This ensures:
- highest-potential centers are tested first,
- worst-case expansions happen early,
- early termination becomes extremely effective.

---

## 5. Expand and Early Terminate

For each center `idx` in the priority order:

### Step A: Early Termination Check

Before expanding:

if (bestCase[idx] <= bestTransLen) break;

Meaning:

> If the maximum possible palindrome at this center cannot beat our current best, stop.

This prunes a large portion of the search space.

### Step B: Expand Around Center

Classic expand-around-center:

- move `left` outward  
- move `right` outward  
- stop on mismatch or boundary  

This is identical to the naive algorithm — except we do it **in the right order**.

### Step C: Update Best Palindrome

If a longer palindrome is found:
- update best length
- update center

---

## 6. Mapping Back to Original String

Let:

- `bestCenter` = best center in transformed string  
- `bestTransLen` = palindrome length in transformed string  

Then:

\[
R = \frac{\text{bestTransLen} - 1}{2}
\]

\[
start = \frac{\text{bestCenter} - R}{2}
\]

\[
len = R
\]

Return:

s.substring(start, start + len)

---

## 7. Complexity Analysis

### 7.1 Time Breakdown

| Step | Complexity |
|------|------------|
| Transform string | O(n) |
| Compute bestCase | O(n) |
| Generate priority order | O(n) |
| Expand-around-center | O(n) amortized |
| Early termination | speeds up worst cases |

### 7.2 Why expansion is O(n)

There are only three major scenarios:

---

### **Case 1: No palindromes (e.g., “abcdefghi”)**  
Each center fails after *one* comparison.  
Total comparisons ≈ n.

---

### **Case 2: One large palindrome in middle (e.g., “racecar”)**  
We find the large palindrome early.  
Now bestTransLen is large, so the early termination rule discards all remaining centers.

Total comparisons ≈ size of the palindrome.

---

### **Case 3: Mixed random data**  
Expansions occur at a few promising centers.  
But once bestTransLen reaches a moderate size, early termination prunes the rest.

Total comparisons stay linear.

---

### **Final Time Complexity: O(n)**  
### **Space Complexity: O(n)**  
(For transformed string and bestCase array)

---

## 8. Comparison with Manacher’s Algorithm

| Aspect | This Algorithm | Manacher’s Algorithm |
|--------|----------------|----------------------|
| Time | O(n) | O(n) |
| Concept | Priority + early termination | Symmetry + mirror reuse |
| Complexity | Simple, intuitive | More complex |
| Reuse of radii | No | Yes |
| Implementation difficulty | Low–medium | High |
| When useful | Teaching, practical coding | Competitive/optimal coding |

Both run in O(n), but the philosophy is different:
- Manacher reuses information via symmetry.
- This algorithm exploits **priority and pruning**.

---

## 9. Conclusion

This work introduces a **new O(n) algorithm** for the Longest Palindromic Substring that is:

- simple to understand,
- based on intuitive best-case reasoning,
- guided by a center-outward priority order,
- accelerated through early termination,
- fully linear in time,
- and independent of Manacher’s symmetry machinery.

The combination of:
1. Transformation  
2. Best-case potential  
3. Priority center selection  
4. Expand-around-center  
5. Early termination  

produces a clean, modern, and intuitive linear-time solution.

Possible future work includes:
- full formal amortized proof,
- empirical benchmarking against Manacher’s algorithm,
- and applying priority-based reasoning to other string/matching problems.

---

## 10. References

- Manacher, G. (1975). *A new linear-time "on-line" algorithm for finding the longest palindromic substring*.  
- Common expand-around-center solutions on LeetCode Problem #5.

---