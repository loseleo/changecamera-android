#!/usr/bin/env bash

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     machine=Linux;;
    Darwin*)    machine=Mac;;
    CYGWIN*)    machine=Cygwin;;
    MINGW*)     machine=MinGw;;
    *)          machine="UNKNOWN:${unameOut}"
esac

if [ "$PKG_DEBUG" != "" ];then
    set -x
fi

# flags
set -e

# variables
key=$1
base_dir="$( cd "$( dirname "$0" )" && pwd )"
pkg_dir="${base_dir}/ddvideoconfigs/packages/${key}"
app_src="$base_dir/app/src"
app_src_main="$app_src/main"
common_src="$base_dir/common/src"
common_src_main="$common_src/main"

# functions

log(){
    echo "[pkg]: $@"
}

mlog(){
    echo -e "[pkg]: $@"
}

clean(){
    log "Clean files: $@"
    rm -rf $@
}

copy(){
    source=$1
    target=$2
    log "Copy files: $@"
    if [ -d "$source" ]; then
        cp -rf $1 $2
    fi
    if [ -f "$source" ]; then
        cp -rf $1 $2
    fi
}

# 清理掉同名文件
clean_sp_name(){
   source=$1
   target=$2
   for i in $(ls "$source"); do
        filename=$(echo $i | sed -e 's#\.[A-Za-z]*$##g')
        rm -rf "${target}${filename}."*
   done
}


xml_filler(){

    log "Handle strings.xml"
    origin_file=$1
    pkg_file=$2

    if [ ! -f "$origin_file" ]; then
        log "xml file not exists";
        exit 0
    fi

    if [ ! -f "$pkg_file" ]; then
        log "xml file not exists";
        exit 0
    fi

    apply_list=$(
        cat "$pkg_file" | grep -E -o '<string\s*name="[^"]*">[^<>]*</string>'
    )

    rm_list=$(
        cat "$pkg_file" | grep -E "^\s*<string" | grep -E -o 'name="[^"]*"'
    )

    origin_strings_xml="$origin_file"

    content=$(cat "${origin_strings_xml}");



    # 先删掉
    for item in $(echo -e "$rm_list"); do
        content=$( echo -e "$content" | grep -v "$item" )
    done


    # 移除掉尾部
    content=$(echo -e "$content" | sed -e "s#</resources>##g")

    # 增加配置中的条目
    IFS=$'\n'
    for line in $(echo -e "$apply_list"); do
        content=$(echo -e "$content\n    $line")
        log "replace or add line: $line"
    done;


    # 补齐尾部
    content="${content}\n</resources>"
    #echo $content

    echo -e "$content" > "${origin_strings_xml}"

}

read_pkg_name(){
    cat "$pkg_dir/gradle.properties" | grep 'PACKAGE_NAME_ANDROID=.*' | awk -F'=' '{print $2}'
}

# 处理微信登录相关
handle_wx_entry(){
    local pkg_name=$1
    dirs=$(echo "$pkg_name" | sed -e 's#\.#/#g')
    file=$(find app/src/main/java -name WXEntryActivity.java)
    file_content=$(cat "$file")
    rm -rf $file
    find app/src/main/java -type d -empty -delete
    wxapi_dir="app/src/main/java/${dirs}/wxapi"
    mkdir -p "$wxapi_dir"
    result_content=$(echo -e "$file_content" | sed -e 's#package ..*\.wxapi#package '"${pkg_name}.wxapi"'#g')
    echo -e "$result_content" > "${wxapi_dir}/WXEntryActivity.java"
}
# 处理微信支付相关
handle_wx_pay_entry(){
    local pkg_name=$1
    dirs=$(echo "$pkg_name" | sed -e 's#\.#/#g')
    file=$(find app/src/main/java -name WXPayEntryActivity.java)
    file_content=$(cat "$file")
    rm -rf $file
    find app/src/main/java -type d -empty -delete
    wxapi_dir="app/src/main/java/${dirs}/wxapi"
    mkdir -p "$wxapi_dir"
    result_content=$(echo -e "$file_content" | sed -e 's#package ..*\.wxapi#package '"${pkg_name}.wxapi"'#g')
    echo -e "$result_content" > "${wxapi_dir}/WXPayEntryActivity.java"

}

handle_manifest(){
    local pkg_name=$1
    if [ "$machine" = "Mac" ]; then
        sed -i '' -e 's#android:name=".*WXEntryActivity"#android:name="'"$pkg_name.wxapi.WXEntryActivity"'"#' app/src/main/AndroidManifest.xml
        sed -i '' -e 's#android:name=".*WXPayEntryActivity"#android:name="'"$pkg_name.wxapi.WXPayEntryActivity"'"#' app/src/main/AndroidManifest.xml
    else
        sed -i 's#android:name=".*WXEntryActivity"#android:name="'"$pkg_name.wxapi.WXEntryActivity"'"#' app/src/main/AndroidManifest.xml
        sed -i 's#android:name=".*WXPayEntryActivity"#android:name="'"$pkg_name.wxapi.WXPayEntryActivity"'"#' app/src/main/AndroidManifest.xml
    fi
}
# entry
if [ -z "$key" ]; then
    pkg_list="$(ls "$base_dir/ddvideoconfigs/packages")"
    log "Options: "
    mlog "$pkg_list"
    log
    log "usage:"
    log "./pkg_make.sh OPTION"
    exit 1
fi

ls -al ddvideoconfigs/packages
log "clone ddvideoconfigs success ------------- "

copy $pkg_dir/res/drawable "$common_src_main/res/"
clean_sp_name "$pkg_dir/res/drawable-xxhdpi/" "$common_src_main/res/drawable-xxhdpi/"
copy "$pkg_dir/res/drawable-xxhdpi" "$app_src_main/res/"
copy "$pkg_dir/res/common/drawable-hdpi" "$common_src_main/res/"
copy "$pkg_dir/res/common/drawable-xhdpi" "$common_src_main/res/"
copy "$pkg_dir/res/common/drawable-xxhdpi" "$common_src_main/res/"
copy "$pkg_dir/gradle.properties" "$base_dir/gradle.properties"
copy "$pkg_dir/config.gradle" "$base_dir/config.gradle"

pkg_name=$(read_pkg_name)
log "pkg_name $pkg_name"
handle_wx_entry $pkg_name
handle_wx_pay_entry $pkg_name
handle_manifest $pkg_name
xml_filler "$common_src_main/res/values/strings.xml" "$pkg_dir/res/values/strings.xml"