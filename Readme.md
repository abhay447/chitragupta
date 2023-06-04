# Chitragupta

Chitragupta is a toy system for testing hypothesis related to customer user journey analysis.

Current objective is to build capability to estimate the number of user completing a customer user journey (CUJ) in a given time period where ordering of events matters (like strict funnel analysis).

## Pending items
1. Write a batch/spark job to calculate exact number of users completing a CUJ.
2. Compare the result of the above spark job with intersection outputs from druid.
3. replace println with proper logging statements