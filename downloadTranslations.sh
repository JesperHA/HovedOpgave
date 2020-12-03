#!/bin/sh

api_token=$1

if [ -z "$api_token" ]
  then
    echo "Please specify a POEditor api_token as the first argument"
    exit
fi

gem install poesie


project_id=157301
languages=( "en" "ru" "sv" "nl" "da" "nb" "es" "is" "pl" "tr" "fi" "it" "ar" "es-mx" )

for language in ${languages[@]}
do
    echo "\n------------------------\nDownloading translations for" $language "\n"
    
    if [ "$language" = "en" ]
        then
        poesie -t $api_token -p $project_id -l $language -a app/src/main/res/values/strings.xml
    elif [ "$language" = "es-mx" ]
        then
        poesie -t $api_token -p $project_id -l $language -a app/src/main/res/values-es-rMX/strings.xml
    elif [ "$language" = "es" ]
        then
        poesie -t $api_token -p $project_id -l $language -a app/src/main/res/values-$language/strings.xml
        poesie -t $api_token -p $project_id -l $language -a app/src/main/res/values-ca/strings.xml
    else
        poesie -t $api_token -p $project_id -l $language -a app/src/main/res/values-$language/strings.xml
    fi
        
done
