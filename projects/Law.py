# -*- coding:utf-8 -*-
import urllib
import urllib2
import re
import os
import json
import sys
class UrlPatch:
    def patchUrl(self, url):
        try:
            request = urllib2.Request(url)
            response = urllib2.urlopen(request)
            content = response.read()
            #content = unicode(content, "gbk").encode("utf-8")
            return content
        except urllib2.URLError, e:
            if hasattr(e,"code"):
                print e.code
            if hasattr(e,"reason"):
                print e.reason + url
    def removeHtml(self, content):
        p = re.compile("<[^>]*br[^>]*>", re.I)#替换<br>为换行符\n
        content = p.sub('\n', content)
        p = re.compile("<[^>]*>")#移除标签
        content = p.sub('', content)
        content = content.replace("&nbsp;", "")
        p = re.compile("[ ]+")#移除空格
        content = p.sub(' ', content)
        p = re.compile("[\f\n\r\t\v]+")#移除空行
        content = p.sub('\n', content)
        return content.strip()
    def getData(self, content, rootpath, curfile):
        #print content
        if content is None:
            return
        pattern = re.compile("<table[^>]*>[\s\S]*?</table>", re.I)
        items = re.findall(pattern, content)
        content = items[1]
        self.parseData(content, rootpath, curfile)
    def parseData(self, contents, rootpath, curfile):
        path = "laws/%s/" % rootpath
        if not os.path.exists(path):
            os.mkdir(path)
        try:
            path = path + curfile
            print path
            file = open(path.decode("utf-8"), 'w')#
        except  Exception,e:
            print "!!!!!!!!!!!!",e
            file = open("error", "w+")
            try:
                print e
                file.write(""+str(e))
                file.write(rootpath)
            finally:
                file.close()
                return
        try:
            file.write(contents)
        finally:
             file.close( )

    def parselawItem(self, item, itemUrl, lawJson, keyName):
        pattern = re.compile(itemUrl + '.*?htm')
        urls = re.findall(pattern, item)
        if len(urls) <= 0:
            return
        urls = urls[0]
        #print urls
        lawInfo = {}#url: time:
        #lawInfo['url'] = urls
        pattern = re.compile('<a[\s\S]+?</a>', re.I)
        lawName = re.findall(pattern, item)
        if len(lawName) < 1:
            print "!!!!!!!!!!!error law name", len(lawName)
        else:
            lawName = self.removeHtml(lawName[0])
            keyName[urls] = lawName;
        pattern = re.compile('<font[\s\S]+?</font>', re.I)
        lawTime = re.findall(pattern, item)
        lawContent = []
        for lt in lawTime:
            cont = self.removeHtml(lt)
            cont = cont.strip()
            if len(cont) > 0 and '' != cont:
                lawContent.append(cont)
        #print len(lawContent), rootUrl+urls,urls, itemUrl
        if len(lawContent) > 4:
            lawtime = lawContent[-1]+lawContent[-2]
            lawInfo['time'] = lawtime
        elif len(lawContent) > 1:
            lawtime = lawContent[-1]
            lawInfo['time'] = lawtime
        else:
            lawInfo['time'] = 'None'
        lawJson[urls] = lawInfo
        self.getData(self.patchUrl(rootUrl+urls), itemUrl, urls)
    def getUrl2(self, content, rootUrl, itemUrl, keyName, pathFile):
        lawJson = {}#law1:lawInfo
        faguiJson = {}
        pattern = re.compile('<title[\s\S]+?</title>',re.I)
        title = re.findall(pattern, content)
        if len(title) <= 0:
            print "error" + len(title)
        title = ''.join(title)
        title = self.removeHtml(title)
        print title
        keyName[itemUrl] = title;
        pattern = re.compile('<table[\s\S]+?</table>', re.I)
        items = re.findall(pattern, content)
        print rootUrl+itemUrl + ".htm", len(items)
        laws = items[2]
        pattern = re.compile('<td[\s\S]+?</td>', re.I)
        lawItem = re.findall(pattern, laws)
        for item in lawItem:
           self.parselawItem(item, itemUrl, lawJson, keyName)
        if (len(items) > 4 and (items[-2].find("19") or items[-2].find("200"))!=-1) \
                or (len(items) > 3 and (items[1].find("19") or items[1].find("200"))!=-1):
            laws = items[-2]
            pattern = re.compile('<td[\s\S]+?</td>', re.I)
            lawItem = re.findall(pattern, laws)
            for item in lawItem:
               self.parselawItem(item, itemUrl, faguiJson, keyName)
        else:
            print "no intro"
        lawAll = {}
        lawAll['laws'] = lawJson
        lawAll['intro'] = faguiJson
        pathFile[itemUrl] = lawAll


baseUrl = 'http://www.jincao.com/fa/'
urlPatch = UrlPatch()

keyName = {}
pathFile = {}

for i in range(1, 27):
    if i < 10:
        #i=5
        rootUrl = "%s0%d/" % (baseUrl, i)
        itemUrl = "law0%d" % i
        content = urlPatch.patchUrl(rootUrl + itemUrl + ".htm")
        urlPatch.getUrl2(content, rootUrl, itemUrl, keyName, pathFile)
        #break
    else:
        rootUrl = "%s%d/" % (baseUrl, i)
        itemUrl = "law%d" % i
        content = urlPatch.patchUrl(rootUrl + itemUrl + ".htm")
        urlPatch.getUrl2(content, rootUrl, itemUrl, keyName, pathFile)
try:
    file1 = open("keyname.json", 'w')#
    file2 = open("filePath.json", 'w')#
except  Exception,e:
    print e
try:
    file1.write(json.dumps(keyName, ensure_ascii=False))
    file2.write(json.dumps(pathFile, ensure_ascii=False, sort_keys=True))
finally:
    file1.close( )
    file2.close()
print "end"
