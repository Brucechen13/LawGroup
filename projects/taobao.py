# -*- coding: cp936 -*-
__author__ = 'CQC'
# -*- coding:utf-8 -*-

import urllib
import urllib2
import cookielib
import re
import webbrowser

#ģ���¼�Ա���
class Taobao:

    #��ʼ������
    def __init__(self):
        #��¼��URL
        self.loginURL = "https://login.taobao.com/member/login.jhtml"
        #����IP��ַ����ֹ�Լ���IP�����
        self.proxyURL = 'http://120.193.146.97:843'
        #��¼POST����ʱ���͵�ͷ����Ϣ
        self.loginHeaders =  {
            'Host':'login.taobao.com',
            'User-Agent' : 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0',
            'Referer' : 'https://login.taobao.com/member/login.jhtml',
            'Content-Type': 'application/x-www-form-urlencoded',
            'Connection' : 'Keep-Alive'
        }
        #�û���
        self.username = 'cqcre'
        #ua�ַ����������Ա�ua�㷨����ó���������ʱ���,�����,��Ļ�ֱ���,�����,����ƶ�,�����,��ʵ���м��������¼,����ƶ��ļ�¼������ļ�¼�ȵȵ���Ϣ
        self.ua = '191UW5TcyMNYQwiAiwTR3tCf0J/QnhEcUpkMmQ=|Um5Ockt0TXdPc011TXVKdyE=|U2xMHDJ+H2QJZwBxX39Rb1d5WXcrSixAJ1kjDVsN|VGhXd1llXGNaYFhkWmJaYl1gV2pIdUtyTXRKfkN4Qn1FeEF6R31TBQ==|VWldfS0TMw8xDjYWKhAwHiUdOA9wCDEVaxgkATdcNU8iDFoM|VmNDbUMV|V2NDbUMV|WGRYeCgGZhtmH2VScVI2UT5fORtmD2gCawwuRSJHZAFsCWMOdVYyVTpbPR99HWAFYVMpUDUFORshHiQdJR0jAT0JPQc/BDoFPgooFDZtVBR5Fn9VOwt2EWhCOVQ4WSJPJFkHXhgoSDVIMRgnHyFqQ3xEezceIRkmahRqFDZLIkUvRiEDaA9qQ3xEezcZORc5bzk=|WWdHFy0TMw8vEy0UIQE0ADgYJBohGjoAOw4uEiwXLAw2DThuOA==|WmBAED5+KnIbdRh1GXgFQSZbGFdrUm1UblZqVGxQa1ZiTGxQcEp1I3U=|W2NDEz19KXENZwJjHkY7Ui9OJQsre09zSWlXY1oMLBExHzERLxsuE0UT|XGZGFjh4LHQdcx5zH34DRyBdHlFtVGtSaFBsUmpWbVBkSmpXd05zTnMlcw==|XWdHFzl5LXUJYwZnGkI/VitKIQ8vEzMKNws3YTc=|XmdaZ0d6WmVFeUB8XGJaYEB4TGxWbk5yTndXa0tyT29Ta0t1QGBeZDI='
        #���룬�����ﲻ��������ʵ���룬�Ա��Դ���������˼��ܴ���256λ���˴�Ϊ���ܺ������
        self.password2 = '7511aa6854629e45de220d29174f1066537a73420ef6dbb5b46f202396703a2d56b0312df8769d886e6ca63d587fdbb99ee73927e8c07d9c88cd02182e1a21edc13fb8e140a4a2a4b53bf38484bd0e08199e03eb9bf7b365a5c673c03407d812b91394f0d3c7564042e3f2b11d156aeea37ad6460118914125ab8f8ac466f'
        self.post = post = {
            'ua':self.ua,
            'TPL_checkcode':'',
            'CtrlVersion': '1,0,0,7',
            'TPL_password':'',
            'TPL_redirect_url':'http://i.taobao.com/my_taobao.htm?nekot=udm8087E1424147022443',
            'TPL_username':self.username,
            'loginsite':'0',
            'newlogin':'0',
            'from':'tb',
            'fc':'default',
            'style':'default',
            'css_style':'',
            'tid':'XOR_1_000000000000000000000000000000_625C4720470A0A050976770A',
            'support':'000001',
            'loginType':'4',
            'minititle':'',
            'minipara':'',
            'umto':'NaN',
            'pstrong':'3',
            'llnick':'',
            'sign':'',
            'need_sign':'',
            'isIgnore':'',
            'full_redirect':'',
            'popid':'',
            'callback':'',
            'guf':'',
            'not_duplite_str':'',
            'need_user_id':'',
            'poy':'',
            'gvfdcname':'10',
            'gvfdcre':'',
            'from_encoding ':'',
            'sub':'',
            'TPL_password_2':self.password2,
            'loginASR':'1',
            'loginASRSuc':'1',
            'allp':'',
            'oslanguage':'zh-CN',
            'sr':'1366*768',
            'osVer':'windows|6.1',
            'naviVer':'firefox|35'
        }
        #��POST�����ݽ��б���ת��
        self.postData = urllib.urlencode(self.post)
        #���ô���
        self.proxy = urllib2.ProxyHandler({'http':self.proxyURL})
        #����cookie
        self.cookie = cookielib.LWPCookieJar()
        #����cookie������
        self.cookieHandler = urllib2.HTTPCookieProcessor(self.cookie)
        #���õ�¼ʱ�õ���opener������open�����൱��urllib2.urlopen
        self.opener = urllib2.build_opener(self.cookieHandler,self.proxy,urllib2.HTTPHandler)
        #��ֵJ_HToken
        self.J_HToken = ''
        #��¼�ɹ�ʱ����Ҫ��Cookie
        self.newCookie = cookielib.CookieJar()
        #��½�ɹ�ʱ����Ҫ��һ���µ�opener
        self.newOpener = urllib2.build_opener(urllib2.HTTPCookieProcessor(self.newCookie))


    #�õ��Ƿ���Ҫ������֤�룬����������Ӧ��ʱ�᲻ͬ����ʱ��Ҫ��֤��ʱ����Ҫ
    def needCheckCode(self):
        #��һ�ε�¼��ȡ��֤�볢�ԣ�����request
        request = urllib2.Request(self.loginURL,self.postData,self.loginHeaders)
        #�õ���һ�ε�¼���Ե���Ӧ
        response = self.opener.open(request)
        #��ȡ���е�����
        content = response.read().decode('gbk')
        #��ȡ״̬��
        status = response.getcode()
        #״̬��Ϊ200����ȡ�ɹ�
        if status == 200:
            print u"��ȡ����ɹ�"
            #\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801������������������֤���utf-8����
            pattern = re.compile(u'\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801',re.S)
            result = re.search(pattern,content)
            #����ҵ����ַ���������Ҫ������֤��
            if result:
                print u"�˴ΰ�ȫ��֤�쳣������Ҫ������֤��"
                return content
            #������Ҫ
            else:
                #���ؽ��ֱ�Ӵ���J_HToken����������ֱ����֤ͨ��
                tokenPattern = re.compile('id="J_HToken" value="(.*?)"')
                tokenMatch = re.search(tokenPattern,content)
                if tokenMatch:
                    self.J_HToken = tokenMatch.group(1)
                    print u"�˴ΰ�ȫ��֤ͨ��������β���Ҫ������֤��"
                    return False
        else:
            print u"��ȡ����ʧ��"
            return None

    #�õ���֤��ͼƬ
    def getCheckCode(self,page):
        #�õ���֤���ͼƬ
        pattern = re.compile('<img id="J_StandardCode_m.*?data-src="(.*?)"',re.S)
        #ƥ��Ľ��
        matchResult = re.search(pattern,page)
        #�Ѿ�ƥ��õ����ݣ�������֤��ͼƬ���Ӳ�Ϊ��
        if matchResult and matchResult.group(1):
            return matchResult.group(1)
        else:
            print u"û���ҵ���֤������"
            return False


    #������֤�룬�������������֤�ɹ����򷵻�J_HToken
    def loginWithCheckCode(self):
        #��ʾ�û�������֤��
        checkcode = raw_input('��������֤��:')
        #����֤��������ӵ�post��������
        self.post['TPL_checkcode'] = checkcode
        #��post�������½��б���
        self.postData = urllib.urlencode(self.post)
        try:
            #�ٴι������󣬼�����֤��֮��ĵڶ��ε�¼����
            request = urllib2.Request(self.loginURL,self.postData,self.loginHeaders)
            #�õ���һ�ε�¼���Ե���Ӧ
            response = self.opener.open(request)
            #��ȡ���е�����
            content = response.read().decode('gbk')
            #�����֤������������ʽ��\u9a8c\u8bc1\u7801\u9519\u8bef ����֤���������ֵı���
            pattern = re.compile(u'\u9a8c\u8bc1\u7801\u9519\u8bef',re.S)
            result = re.search(pattern,content)
            #�������ҳ������ˣ���֤����������
            if result:
                print u"��֤���������"
                return False
            else:
                #���ؽ��ֱ�Ӵ���J_HToken������˵����֤������ɹ����ɹ���ת���˻�ȡHToken�Ľ���
                tokenPattern = re.compile('id="J_HToken" value="(.*?)"')
                tokenMatch = re.search(tokenPattern,content)
                print content
                #���ƥ��ɹ����ҵ���J_HToken
                if tokenMatch:
                    print u"��֤��������ȷ"
                    self.J_HToken = tokenMatch.group(1)
                    return tokenMatch.group(1)
                else:
                    #ƥ��ʧ�ܣ�J_Token��ȡʧ��
                    print u"J_Token��ȡʧ��"
                    return False
        except urllib2.HTTPError, e:
            print u"���ӷ�������������ԭ��",e.reason
            return False


    #ͨ��token���st
    def getSTbyToken(self,token):
        tokenURL = 'https://passport.alipay.com/mini_apply_st.js?site=0&token=%s&callback=stCallback6' % token
        request = urllib2.Request(tokenURL)
        response = urllib2.urlopen(request)
        #����st������û��Ա���ҳ�ĵ�¼��ַ
        pattern = re.compile('{"st":"(.*?)"}',re.S)
        result = re.search(pattern,response.read())
        #����ɹ�ƥ��
        if result:
            print u"�ɹ���ȡst��"
            #��ȡst��ֵ
            st = result.group(1)
            return st
        else:
            print u"δƥ�䵽st"
            return False

    #����st����е�¼,��ȡ�ض�����ַ
    def loginByST(self,st,username):
        stURL = 'https://login.taobao.com/member/vst.htm?st=%s&TPL_username=%s' % (st,username)
        headers = {
            'User-Agent' : 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0',
            'Host':'login.taobao.com',
            'Connection' : 'Keep-Alive'
        }
        request = urllib2.Request(stURL,headers = headers)
        response = self.newOpener.open(request)
        content =  response.read().decode('gbk')
        #����������Ƿ��¼�ɹ�
        pattern = re.compile('top.location = "(.*?)"',re.S)
        match = re.search(pattern,content)
        if match:
            print u"��¼��ַ�ɹ�"
            location = match.group(1)
            return True
        else:
            print "��¼ʧ��"
            return False


    #������򵽵ı���ҳ��
    def getGoodsPage(self,pageIndex):
        goodsURL = 'http://buyer.trade.taobao.com/trade/itemlist/listBoughtItems.htm?action=itemlist/QueryAction&event_submit_do_query=1' + '&pageNum=' + str(pageIndex)
        response = self.newOpener.open(goodsURL)
        page =  response.read().decode('gbk')
        return page



    #������������
    def main(self):
        #�Ƿ���Ҫ��֤�룬����õ�ҳ�����ݣ������򷵻�False
        needResult = self.needCheckCode()
        #�����ȡʧ�ܣ��õ��Ľ����None
        if not needResult ==None:
            if not needResult == False:
                print u"����Ҫ�ֶ�������֤��"
                checkCode = self.getCheckCode(needResult)
                #�õ�����֤�������
                if not checkCode == False:
                    print u"��֤���ȡ�ɹ�"
                    print u"�������������������������֤��"
                    webbrowser.open_new_tab(checkCode)
                    self.loginWithCheckCode()
                #��֤������Ϊ�գ���Ч��֤��
                else:
                    print u"��֤���ȡʧ�ܣ�������"
            else:
                print u"����Ҫ������֤��"
        else:
            print u"�����¼ҳ��ʧ�ܣ��޷�ȷ���Ƿ���Ҫ��֤��"


        #�ж�token�Ƿ�������ȡ��
        if not self.J_HToken:
            print "��ȡTokenʧ�ܣ�������"
            return
        #��ȡst��
        st = self.getSTbyToken(self.J_HToken)
        #����st���е�¼
        result = self.loginByST(st,self.username)
        if result:
            #������б�����ҳ��
            page = self.getGoodsPage(1)
            print page
        else:
            print u"��¼ʧ��"



taobao = Taobao()
taobao.main()
