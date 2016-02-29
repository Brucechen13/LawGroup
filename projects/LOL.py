# -*- coding:utf-8 -*-
import urllib
import urllib2
import re

sum = 0
class Data:
    def patchUrl(self, url):
        try:
            user_agent = 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0'
            headers = { 'User-Agent' : user_agent }
            request = urllib2.Request(url,headers = headers)
            page = urllib2.urlopen(request).read().decode("utf-8")
            #print page
            return page
        except urllib2.URLError, e:
            if hasattr(e,"code"):
                print e.code
            if hasattr(e,"reason"):
                print e.reason
    def getUrl(self, content, index):
        pattern = re.compile('<span class="rating_num" property="v:average">(\d\.\d)</span>', re.S)
        items = re.findall(pattern, content)
        sum = 0
        for item in items:
            print item
            sum += float(item)
            index += 1
            if index == 166:
                print "end: ", index
                break
        return sum
data = Data()
for i in range(0, 7):
    print i, i*25
    content = data.patchUrl("http://movie.douban.com/top250?start="+str(i*25))
    sum += data.getUrl(content, i*25)
print "sum: ", sum