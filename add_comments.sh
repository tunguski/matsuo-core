#!/bin/bash

# CI_PROJECT_ID

HOST=http://gitlab.matsuo.pl

PROJECT_ID=$(curl -H "PRIVATE-TOKEN: $USER_PRIVATE_TOKEN" $HOST/ci/api/v1/projects | python -m json.tool | grep -E "gitlab_id|\"id\"" | grep "id\": $CI_PROJECT_ID," -B 1 | grep "\"gitlab_id\"" | cut -d ":" -f 2 | cut -d "," -f 1 | sed -e 's/^[[:space:]]*//')
COMMIT_ID=$CI_BUILD_REF

CHECKSTYLE=$(cat target/checkstyle-result.xml  | grep "<error " | wc -l)
PMD=$(cat target/pmd.xml | grep "<violation" | wc -l)
FIND_BUGS=$(cat target/findbugsXml.xml | sed 's/total_bugs/\ntotal_bugs/g' | cut -f 1 -d " " | grep total_bugs | cut -f 2 -d "'" | head -n 1)


URL=$HOST/api/v3/projects/$PROJECT_ID/repository/commits/$COMMIT_ID/comments

echo $URL

read -r -d '' NOTE << EOM
# Build result

| Type | Result |
| ----: | ----- |
| **Checkstyle** | $CHECKSTYLE |
| **Pmd** | $PMD |
| **Find Bugs** | $FIND_BUGS |
| **Cobertura** | |
EOM

for i in `cat target/site/cobertura/coverage.xml | grep lines-covered | cut -d " " -f 2- | sed 's/ /\n/g'`
do 
  TMP=$(echo $i | sed 's/[">]//g' | sed 's/=/ \| /g' | sed 's/^/\| /g' | sed 's/$/ \|/g')
  TMP+=$'\n'
  NOTE="$NOTE$TMP"
done

curl --data $"note=$NOTE" \
    -H "PRIVATE-TOKEN: $USER_PRIVATE_TOKEN" \
    $URL 