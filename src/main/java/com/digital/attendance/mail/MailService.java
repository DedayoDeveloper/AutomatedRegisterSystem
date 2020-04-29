/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.mail;

import com.digital.attendance.model.Mail;

/**
 *
 * @author oreoluwa
 */
public interface MailService {
    
    public void sendEmail(Mail mail);
    
}
