# -*- coding:utf-8 -*-
import urllib
import urllib2
import re

class Data:

    def patchUrl(self, url):
        try:
            request = urllib2.Request(url)
            response = urllib2.urlopen(request)
            content = response.read().decode('utf-8')
            return content
        except urllib2.URLError, e:
            if hasattr(e,"code"):
                print e.code
            if hasattr(e,"reason"):
                print e.reason
    def getData(self, content):
        pattern = re.compile('class="box listFar">.*?<h1>(.*?)</h1>')
        items = re.findall(pattern, content)
        for item in items:
            print item
    def getUrl(self, content):
        pattern = re.compile('"url_zb":"(.*?)",', re.S)
        items = re.findall(pattern, content)
        for item in items:
            item = item.replace('/', '')
            item = item.replace('\\', '/')
            print item
            self.getData(self.patchUrl(item))
    
        

data = Data()
content = data.patchUrl("http://lol.15w.com/zt/2015lplsummer/match_data/index_2.html?time=1439122000")
data.getUrl(content)
