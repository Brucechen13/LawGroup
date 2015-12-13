# -*- coding: cp936 -*-
__author__ = 'CQC'
# -*- coding:utf-8 -*-
import urllib
import urllib2
import re

#�ٶ�����������
class BDTB:

    #��ʼ�����������ַ���Ƿ�ֻ��¥���Ĳ���
    def __init__(self,baseUrl,seeLZ):
        self.baseURL = baseUrl
        self.seeLZ = '?see_lz='+str(seeLZ)

    #��ȡ���ӱ���
    def getTitle(self):
        page = self.getPage(1)
        print(page)
        pattern = re.compile('<h3 class="core_title_txt.*?>(.*?)</h3>',re.S)
        result = re.search(pattern,page)
        if result:
            #print result.group(1)  #�������
            return result.group(1).strip()
        else:
            return None

    #����ҳ�룬��ȡ��ҳ���ӵĴ���
    def getPage(self,pageNum):
        try:
            url = self.baseURL+ self.seeLZ + '&pn=' + str(pageNum)
            request = urllib2.Request(url)
            response = urllib2.urlopen(request)
            print "get page"
            return response.read().decode('utf-8')
        except urllib2.URLError, e:
            if hasattr(e,"reason"):
                print u"���Ӱٶ�����ʧ��,����ԭ��",e.reason
                return None

baseURL = 'http://tieba.baidu.com/p/3138733512'
bdtb = BDTB(baseURL,1)
print bdtb.getTitle()
