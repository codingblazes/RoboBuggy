/********************************************************************************
 * All rights are open to the public, to use, share, dump, spread for the       *
 * foreseeable future. This software and documentation constitutes purely 		*
 * published and public work, and contains open source knowledge by a bunch		*
 * of college kids who just want to have fun. All the material and code may be 	*
 * used, copied, duplicated, changed, disclosed... with any human's free will.	*
 * Have a nice day! :)															*
 ********************************************************************************/

/**
 * @author: Ian Hartwig (spacepenguine@gmail.com)
 * @author: Audrey Yeoh (ayeohmy@gmail.com)
 * @date: 10/3/2014
 */

#include "rbserialmessages.h"


// Public //////////////////////////////////////////////////////////////////////

RBSerialMessages::RBSerialMessages() {
}


int RBSerialMessages::Init(UARTFILE *in_file, FILE *out_file) {
    // setup the hardware serial
    in_file_ = in_file;
    out_file_ = out_file;

    // init the input buffer
    InitReadBuffer();

    // success
    return 1;
}

int RBSerialMessages::Send(uint8_t id, uint32_t message) {
    uint8_t buffer_pos;
    buffer_pos = InitMessageBuffer();
    buffer_pos = AppendMessageToBuffer(id, message, buffer_pos);

    for(int i = 0; i < buffer_pos; i++) {
        fputc(buffer_out_[i], out_file_);
    }

    // success
    return 0;
}


// return 0 if a complete message was found
// return -1 if not enough data was found
// return -2 if an invalid message was found
int RBSerialMessages::Read(rb_message_t* read_message) {
    while(in_file_->available() > 0) {
        // check if there is new data
        char new_serial_byte = fgetc(in_file_);

        // first, we need to try to lock on to the stream
        if(buffer_in_stream_lock_ == false) {
            printf("%s: searching for lock...\n", __PRETTY_FUNCTION__);
            if((uint8_t)new_serial_byte == FOOTER) {
                printf("%s: got lock!\n", __PRETTY_FUNCTION__);
                buffer_in_stream_lock_ = true;
            }
        // after we lock we need to read in to the buffer until full
        } else {
            // this->Send(RBSM_MID_ERROR, buffer_in_pos_);
            buffer_in_[buffer_in_pos_] = (uint8_t)new_serial_byte; 
            buffer_in_pos_++;
            // handle the end of a packet
            if(buffer_in_pos_ == RBSM_PACKET_LENGTH) {
                // reset buffer for next packet
                buffer_in_pos_ = 0;
                // parse this complete packet
                if(buffer_in_[5] == FOOTER) {
                    read_message->message_id = buffer_in_[0];
                    uint8_t *data_bytes = (uint8_t *) &(read_message->data);
                    data_bytes[0] = buffer_in_[4];
                    data_bytes[1] = buffer_in_[3];
                    data_bytes[2] = buffer_in_[2];
                    data_bytes[3] = buffer_in_[1];
                    return 0;
                // skip packet as an error
                } else {
                    buffer_in_stream_lock_ = false;
                    return RBSM_ERROR_INVALID_MESSAGE;
                }
            } 
        }
    }
    // we didn't have enough data to read a whole packet
    return RBSM_ERROR_INSUFFICIENT_DATA;

    // maybe check if the footer matches buffer_in_, else throw the message cause it is sad.
}

// Private /////////////////////////////////////////////////////////////////////
uint8_t RBSerialMessages::AppendMessageToBuffer(uint8_t id,
                                                uint32_t message,
                                                uint8_t out_start_pos) {
    uint8_t buffer_pos = out_start_pos;
    uint8_t message_ll = message & RBSM_ONE_BYTE_MASK;
    uint8_t message_lh = (message >> RBSM_ONE_BYTE_SIZE) & RBSM_ONE_BYTE_MASK;
    uint8_t message_hl = (message >> RBSM_TWO_BYTE_SIZE) & RBSM_ONE_BYTE_MASK;
    uint8_t message_hh = (message >> RBSM_THREE_BYTE_SIZE) & RBSM_ONE_BYTE_MASK;

    // write message id (and id since single message)
    buffer_out_[buffer_pos++] = id;

    buffer_out_[buffer_pos++] = message_hh;
    buffer_out_[buffer_pos++] = message_hl;
    buffer_out_[buffer_pos++] = message_lh;
    buffer_out_[buffer_pos++] = message_ll;

    buffer_out_[buffer_pos++] = FOOTER;

    // write null terminator just in case. note no increment
    buffer_out_[buffer_pos] = RBSM_NULL_TERM;


    return buffer_pos;
}


uint8_t RBSerialMessages::InitMessageBuffer() {
    uint8_t buffer_pos = 0;

    // write null terminator just in case. note no increment
    buffer_out_[buffer_pos] = RBSM_NULL_TERM;

    return buffer_pos;
}


int RBSerialMessages::InitReadBuffer() {
    buffer_in_pos_ = 0;
    buffer_in_stream_lock_ = false;

    return 0;
}
